'use strict';

angular.module('myApp', [
  'ngRoute',
  'ngMaterial',
  'myApp.products',
  'angularBootstrapNavTree'
]).config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {
  $locationProvider.hashPrefix('!');
  $routeProvider.otherwise({redirectTo: '/products'});
}]);