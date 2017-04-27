angular.module('starter.controllers', [])

.controller('AppCtrl', function($scope, $ionicModal, $timeout) {

  // With the new view caching in Ionic, Controllers are only called
  // when they are recreated or on app start, instead of every page change.
  // To listen for when this page is active (for example, to refresh data),
  // listen for the $ionicView.enter event:
  //$scope.$on('$ionicView.enter', function(e) {
  //});

  // Form data for the login modal
  $scope.loginData = {};

  // Create the login modal that we will use later
  $ionicModal.fromTemplateUrl('templates/login.html', {
    scope: $scope
  }).then(function(modal) {
    $scope.modal = modal;
  });

  // Triggered in the login modal to close it
  $scope.closeLogin = function() {
    $scope.modal.hide();
  };

  // Open the login modal
  $scope.login = function() {
    $scope.modal.show();
  };

  // Perform the login action when the user submits the login form
  $scope.doLogin = function() {
    console.log('Doing login', $scope.loginData);

    // Simulate a login delay. Remove this and replace with your login
    // code if using a login system
    $timeout(function() {
      $scope.closeLogin();
    }, 1000);
  };
})

.controller('PlaylistsCtrl', function($scope) {
  $scope.playlists = [
    { title: 'People who were born in a certain place before a certain year', id: 1 },
    { title: 'People who died in a different place than the one they were born in', id: 2 },
  ];
})

.controller('FirstQueryRecordsCtrl', function($scope) {
  $scope.records = [];
})


.controller('FirstQueryRecordsCtrl', function($scope) {
  $scope.records = [];
})

.controller('ShowResultsButtonCtrl',function($scope,$state){
	$scope.showFirstQueryResults = function(place,year){
		   console.log('show results button was clicked');
       console.log(place);
       console.log(year);
		$state.go('query1Results');
	}
})

.controller('PlaylistCtrl', function($scope, $stateParams) {
});

