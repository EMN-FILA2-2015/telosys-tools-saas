(function () {
  "use strict";

  angular
    .module('telosysToolsSaasFrontApp')
    .config(function ($stateProvider) {
      $stateProvider
        .state('home', {
          url: '/',
          data: {
            roles: [],
            pageTitle: 'home.title'
          },
          views: {
            'content@': {
              templateUrl: 'app/home/home.html',
              controller: 'HomeController as home'
            }
          },
          resolve: {
            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('home');
              return $translate.refresh();
            }]
          }
        });
    });

})();



