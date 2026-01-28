import userProfile from 'app/entities/user-profile/user-profile.reducer';
import permission from 'app/entities/permission/permission.reducer';
import userPermission from 'app/entities/user-permission/user-permission.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userProfile,
  permission,
  userPermission,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
