import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-profile.reducer';

export const UserProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userProfileEntity = useAppSelector(state => state.userProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userProfileDetailsHeading">
          <Translate contentKey="morocco2030App.userProfile.detail.title">UserProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.id}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="morocco2030App.userProfile.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.phoneNumber}</dd>
          <dt>
            <span id="birthDate">
              <Translate contentKey="morocco2030App.userProfile.birthDate">Birth Date</Translate>
            </span>
          </dt>
          <dd>
            {userProfileEntity.birthDate ? (
              <TextFormat value={userProfileEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="nationality">
              <Translate contentKey="morocco2030App.userProfile.nationality">Nationality</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.nationality}</dd>
          <dt>
            <span id="preferredLanguage">
              <Translate contentKey="morocco2030App.userProfile.preferredLanguage">Preferred Language</Translate>
            </span>
          </dt>
          <dd>{userProfileEntity.preferredLanguage}</dd>
          <dt>
            <Translate contentKey="morocco2030App.userProfile.user">User</Translate>
          </dt>
          <dd>{userProfileEntity.user ? userProfileEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-profile/${userProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserProfileDetail;
