import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-permission.reducer';

export const UserPermissionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userPermissionEntity = useAppSelector(state => state.userPermission.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userPermissionDetailsHeading">
          <Translate contentKey="morocco2030App.userPermission.detail.title">UserPermission</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userPermissionEntity.id}</dd>
          <dt>
            <span id="grantedAt">
              <Translate contentKey="morocco2030App.userPermission.grantedAt">Granted At</Translate>
            </span>
          </dt>
          <dd>
            {userPermissionEntity.grantedAt ? (
              <TextFormat value={userPermissionEntity.grantedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="morocco2030App.userPermission.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{userPermissionEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="morocco2030App.userPermission.user">User</Translate>
          </dt>
          <dd>{userPermissionEntity.user ? userPermissionEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="morocco2030App.userPermission.permission">Permission</Translate>
          </dt>
          <dd>{userPermissionEntity.permission ? userPermissionEntity.permission.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-permission" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-permission/${userPermissionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserPermissionDetail;
