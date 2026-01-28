import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserPermission from './user-permission';
import UserPermissionDetail from './user-permission-detail';
import UserPermissionUpdate from './user-permission-update';
import UserPermissionDeleteDialog from './user-permission-delete-dialog';

const UserPermissionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserPermission />} />
    <Route path="new" element={<UserPermissionUpdate />} />
    <Route path=":id">
      <Route index element={<UserPermissionDetail />} />
      <Route path="edit" element={<UserPermissionUpdate />} />
      <Route path="delete" element={<UserPermissionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserPermissionRoutes;
