import React from 'react';
import RootTemplate from 'templates/RootTemplate';
import CategoryList from 'components/Category/CategoryList';
import CreateAdminAndDb from 'components/Admin/CreateAdminAndDb';
import Chart from 'components/Chart/Chart';

const AdminPage = () => {
  return (
    <RootTemplate>
      <Chart />
      <CategoryList />
      <CreateAdminAndDb />
    </RootTemplate>
  );
};

export default AdminPage;
