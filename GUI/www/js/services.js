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

.factory('DynamicParams', function () {
	var name={};
	var query={};
	return {
		getName: function () {
			return name.toString();
		},
		setName: function (nameParameter) {
			name = nameParameter;
		},
		getQuery: function () {
			return query.toString();
		},
		setQuery: function (queryParameter) {
			query = queryParameter;
		}
		
	};
})

.factory('Query1ExtraInfo', function () {
	var person={};
	
	return {
		getPerson: function () {
			return person;
		},
		setPerson: function (perParameter) {
			person = perParameter;
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

.factory('CheckQuery2Input',function($q){
	
	
	return{
		isNumber: function(n) {
  		return !isNaN(parseFloat(n)) && isFinite(n);
		},
		validateInput: function(place,year){
			 var deferred = $q.defer();
		//$scope.legalInput=true;
		 if (!(year && place)) {
            //don't allow the user search unless he enters all inputs
          // var InputErrorAlert = $ionicPopup.alert({
			//	title: 'Input error!',
			//	template: 'Missing Input. Please insert valid Place and Year', 
			//});
			deferred.reject('MISSING');
          } 
		else{
			//Input is set, but check that it is legal.
			if( !this.isNumber(year)){
				//var InputErrorAlert = $ionicPopup.alert({
				//title: 'Input error!',
				//template: 'Illegal Input. Please insert a valid Year', 
			//});
				//$scope.legalInput=false;
				deferred.reject('INVALIDYEAR');
			}
		}
		
		deferred.resolve('OK');
		return deferred.promise;
		}
	};
})

//EOF
;


