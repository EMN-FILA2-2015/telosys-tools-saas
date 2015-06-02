/**
 * Created by Adrian on 01/04/2015.
 */

(function () {
  "use strict";

  angular
    .module('telosysToolsSaasFrontApp')
    .config(function ($stateProvider) {
      $stateProvider
        .state('project', {
          parent: 'site',
          url: '/projects/{projectId}',
          data: {
            roles: ['ROLE_USER'],
            pageTitle: 'global.menu.account.settings'
          },
          views: {
            'content@': {
              templateUrl: 'app/project/project.html',
              controller: 'ProjectController as project'
            }
          },
          resolve: {
            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('project');
              return $translate.refresh();
            }]
          }
        });
    });

})();
