import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ClientAccount from './client-account';
import ClientAccountDetail from './client-account-detail';
import ClientAccountUpdate from './client-account-update';
import ClientAccountDeleteDialog from './client-account-delete-dialog';

const ClientAccountRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ClientAccount />} />
    <Route path="new" element={<ClientAccountUpdate />} />
    <Route path=":id">
      <Route index element={<ClientAccountDetail />} />
      <Route path="edit" element={<ClientAccountUpdate />} />
      <Route path="delete" element={<ClientAccountDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ClientAccountRoutes;
