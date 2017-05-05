/* global angular, document, window */
'use strict';

angular.module('starter.services', [])
.factory('User', function() {

  var o = {
    favorites: []
  }

  return o;
});
