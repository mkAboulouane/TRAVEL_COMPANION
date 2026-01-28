import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getPermissions } from 'app/entities/permission/permission.reducer';
import { createEntity, getEntity, reset, updateEntity } from './user-permission.reducer';

export const UserPermissionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const permissions = useAppSelector(state => state.permission.entities);
  const userPermissionEntity = useAppSelector(state => state.userPermission.entity);
  const loading = useAppSelector(state => state.userPermission.loading);
  const updating = useAppSelector(state => state.userPermission.updating);
  const updateSuccess = useAppSelector(state => state.userPermission.updateSuccess);

  const handleClose = () => {
    navigate(`/user-permission${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getPermissions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.grantedAt = convertDateTimeToServer(values.grantedAt);

    const entity = {
      ...userPermissionEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      permission: permissions.find(it => it.id.toString() === values.permission?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          grantedAt: displayDefaultDateTime(),
        }
      : {
          ...userPermissionEntity,
          grantedAt: convertDateTimeFromServer(userPermissionEntity.grantedAt),
          user: userPermissionEntity?.user?.id,
          permission: userPermissionEntity?.permission?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="morocco2030App.userPermission.home.createOrEditLabel" data-cy="UserPermissionCreateUpdateHeading">
            <Translate contentKey="morocco2030App.userPermission.home.createOrEditLabel">Create or edit a UserPermission</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="user-permission-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('morocco2030App.userPermission.grantedAt')}
                id="user-permission-grantedAt"
                name="grantedAt"
                data-cy="grantedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('morocco2030App.userPermission.isActive')}
                id="user-permission-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                id="user-permission-user"
                name="user"
                data-cy="user"
                label={translate('morocco2030App.userPermission.user')}
                type="select"
                required
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="user-permission-permission"
                name="permission"
                data-cy="permission"
                label={translate('morocco2030App.userPermission.permission')}
                type="select"
                required
              >
                <option value="" key="0" />
                {permissions
                  ? permissions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-permission" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default UserPermissionUpdate;
