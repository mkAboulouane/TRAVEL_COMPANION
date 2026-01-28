import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('UserPermission e2e test', () => {
  const userPermissionPageUrl = '/user-permission';
  const userPermissionPageUrlPattern = new RegExp('/user-permission(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const userPermissionSample = {};

  let userPermission;
  // let user;
  // let permission;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"2","firstName":"Florie","lastName":"Leclercq","email":"Ines78@gmail.com","imageUrl":"miaou","langKey":"équipe de "},
    }).then(({ body }) => {
      user = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/permissions',
      body: {"name":"bien mettre de sorte que","description":"persuader","code":"presque pourvu que"},
    }).then(({ body }) => {
      permission = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/user-permissions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-permissions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-permissions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

    cy.intercept('GET', '/api/permissions', {
      statusCode: 200,
      body: [permission],
    });

  });
   */

  afterEach(() => {
    if (userPermission) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-permissions/${userPermission.id}`,
      }).then(() => {
        userPermission = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
    if (permission) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/permissions/${permission.id}`,
      }).then(() => {
        permission = undefined;
      });
    }
  });
   */

  it('UserPermissions menu should load UserPermissions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-permission');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserPermission').should('exist');
    cy.url().should('match', userPermissionPageUrlPattern);
  });

  describe('UserPermission page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userPermissionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserPermission page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-permission/new$'));
        cy.getEntityCreateUpdateHeading('UserPermission');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userPermissionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-permissions',
          body: {
            ...userPermissionSample,
            user: user,
            permission: permission,
          },
        }).then(({ body }) => {
          userPermission = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-permissions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-permissions?page=0&size=20>; rel="last",<http://localhost/api/user-permissions?page=0&size=20>; rel="first"',
              },
              body: [userPermission],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(userPermissionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(userPermissionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details UserPermission page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userPermission');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userPermissionPageUrlPattern);
      });

      it('edit button click should load edit UserPermission page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserPermission');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userPermissionPageUrlPattern);
      });

      it('edit button click should load edit UserPermission page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserPermission');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userPermissionPageUrlPattern);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of UserPermission', () => {
        cy.intercept('GET', '/api/user-permissions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userPermission').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userPermissionPageUrlPattern);

        userPermission = undefined;
      });
    });
  });

  describe('new UserPermission page', () => {
    beforeEach(() => {
      cy.visit(`${userPermissionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserPermission');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of UserPermission', () => {
      cy.get(`[data-cy="grantedAt"]`).type('2026-01-27T11:08');
      cy.get(`[data-cy="grantedAt"]`).blur();
      cy.get(`[data-cy="grantedAt"]`).should('have.value', '2026-01-27T11:08');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="user"]`).select(1);
      cy.get(`[data-cy="permission"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        userPermission = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', userPermissionPageUrlPattern);
    });
  });
});
