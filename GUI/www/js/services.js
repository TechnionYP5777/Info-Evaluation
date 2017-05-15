/* global angular, document, window */

// authors:Genia, Moshe
// since : 6/5/2017
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

//EOF
;


