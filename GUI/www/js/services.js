/* global angular, document, window */
'use strict';

angular.module('starter.services', [])

.factory('DataQ', function () {
	var year = {};
	var place = {};
	return {
		getYear: function () {
			return year;
		},
		setYear: function (yearparameter) {
			year = yearparameter;
		},
		getPlace: function () {
			return place;
		},
		setPlace: function (placeparameter) {
			place = placeparameter;
		}
	};
})
;


