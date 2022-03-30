import React from 'react';
import RootTemplate from 'templates/RootTemplate';
import CategoryList from 'Components/Category/CategoryList';
import CreateAdminAndDb from 'Components/Admin/CreateAdminAndDb';
import Chart from 'Components/Chart/Chart';

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
