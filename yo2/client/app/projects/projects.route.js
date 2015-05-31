/**
 * Created by Killian on 05/02/2015.
 */

(function () {
  "use strict";

  angular
    .module('telosysToolsSaasFrontApp')
    .config(function ($stateProvider) {
      $stateProvider
        .state('projects', {
          parent: 'site',
          url: '/projects',
          data: {
            roles: ['ROLE_USER'],
            pageTitle: 'global.menu.account.settings'
          },
          views: {
            'content@': {
              templateUrl: 'app/projects/projects.html',
              controller: 'ProjectsController as projects'
            }
          },
          resolve: {
            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('projects');
              return $translate.refresh();
            }]
          }
        });
    });

})();
