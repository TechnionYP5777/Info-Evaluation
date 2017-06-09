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
			return year.toString();
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


.factory('ArrestsParams', function () {
	var name={};
	return {
		getName: function () {
			return name.toString();
		},
		setName: function (nameParameter) {
			name = nameParameter;
		}
	};
})

.factory('AwardsParams', function () {
	var name={};
	return {
		getName: function () {
			return name.toString();
		},
		setName: function (nameParameter) {
			name = nameParameter;
		}
	};
})

.factory('Query1ExtraInfo', function () {
	var name={};
	return {
		getName: function () {
			return name.toString();
		},
		setName: function (nameParameter) {
			name = nameParameter;
		}
	};
})


.factory('Query2ExtraInfo', function () {
	var name={};
	return {
		getName: function () {
			return name.toString();
		},
		setName: function (nameParameter) {
			name = nameParameter;
		}
	};
})

//EOF
;


