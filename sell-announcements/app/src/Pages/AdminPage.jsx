import React from 'react';
import RootTemplate from 'templates/RootTemplate';
import CategoryList from 'Components/Category/CategoryList';
import CreateAdminAndDb from 'Components/Admin/CreateAdminAndDb';

const AdminPage = () => {
  return (
    <RootTemplate>
      <CategoryList />
      <CreateAdminAndDb />
    </RootTemplate>
  );
};

export default AdminPage;
