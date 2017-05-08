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


.factory('Query2Results',function($scope,$http,$ionicPopup,ApiEndpoint){
	$scope.persons=[];
    $http({
      method: 'GET',
      url: ApiEndpoint.url+ 'Queries/Query2/',
    }).then(function successCallback(response) {
        $scope.persons = [];
        for(var r in response.data) {
          var person = response.data[r];
          $scope.persons.push(person);
        }

    }, function errorCallback(response) {
        var FetchErrorAlert = $ionicPopup.alert({
            title: 'Fetch error!',
            template: 'Unable to get data',
        });
    });

	//End of this factory
})


//EOF
;


