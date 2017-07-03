/* global angular, document, window */
'use strict';

angular.module('starter.controllers', [])


.constant('ApiEndpoint', {
    url: 'http://132.68.206.107:8080'
	//url: 'http://localhost:8100/api'
})


.controller('AppCtrl', function($scope, $ionicModal, $ionicPopover, $timeout) {
    // Form data for the login modal
    $scope.loginData = {};
    $scope.isExpanded = false;
    $scope.hasHeaderFabLeft = false;
    $scope.hasHeaderFabRight = false;

    var navIcons = document.getElementsByClassName('ion-navicon');
    for (var i = 0; i < navIcons.length; i++) {
        navIcons.addEventListener('click', function() {
            this.classList.toggle('active');
        });
    }

    ////////////////////////////////////////
    // Layout Methods
    ////////////////////////////////////////

    $scope.hideNavBar = function() {
        document.getElementsByTagName('ion-nav-bar')[0].style.display = 'none';
    };

    $scope.showNavBar = function() {
        document.getElementsByTagName('ion-nav-bar')[0].style.display = 'block';
    };

    $scope.noHeader = function() {
        var content = document.getElementsByTagName('ion-content');
        for (var i = 0; i < content.length; i++) {
            if (content[i].classList.contains('has-header')) {
                content[i].classList.toggle('has-header');
            }
        }
    };
    $scope.setExpanded = function(bool) {
        $scope.isExpanded = bool;
    };

    $scope.setHeaderFab = function(location) {
        var hasHeaderFabLeft = false;
        var hasHeaderFabRight = false;

        switch (location) {
            case 'left':
                hasHeaderFabLeft = true;
                break;
            case 'right':
                hasHeaderFabRight = true;
                break;
        }

        $scope.hasHeaderFabLeft = hasHeaderFabLeft;
        $scope.hasHeaderFabRight = hasHeaderFabRight;
    };

    $scope.hasHeader = function() {
        var content = document.getElementsByTagName('ion-content');
        for (var i = 0; i < content.length; i++) {
            if (!content[i].classList.contains('has-header')) {
                content[i].classList.toggle('has-header');
            }
        }

    };

    $scope.hideHeader = function() {
        $scope.hideNavBar();
        $scope.noHeader();
    };

    $scope.showHeader = function() {
        $scope.showNavBar();
        $scope.hasHeader();
    };

    $scope.clearFabs = function() {
        var fabs = document.getElementsByClassName('button-fab');
        if (fabs.length && fabs.length > 1) {
            fabs[0].remove();
        }
    };
})

.controller('LoginCtrl', function($scope, $timeout, $stateParams, ionicMaterialInk) {
    $scope.$parent.clearFabs();
    $timeout(function() {
        $scope.$parent.hideHeader();
    }, 0);
    ionicMaterialInk.displayEffect();
})


.controller('ShowResultsButtonCtrl', function($scope, $state, DataQ, $ionicPopup, CheckQuery2Input) {

    $scope.showSecondQueryResults = function(place, year) {
        console.log('show results button was clicked-query 2');
		console.log('place is: '+place+' year is: '+year);
		if(!place)
			console.log('supposedly empty');
        CheckQuery2Input.validateInput(place, year)
            .then(
                function(response) {
                    if (response == 'OK') {

                        DataQ.setYear(year);
                        DataQ.setPlace(place);
                        $state.go('app.Query2Results');
                    }
                },
                function(error) {
                    if (error == 'MISSING') {
                        //don't allow the user search unless he enters all inputs
                        var InputErrorAlert = $ionicPopup.alert({
                            title: 'Input error!',
                            template: 'Missing Input. Please insert valid Place and Year',
                        });
                    } else if (error == 'INVALIDYEAR') {
                        var InputErrorAlert = $ionicPopup.alert({
                            title: 'Input error!',
                            template: 'Illegal Input. Please check the year and/or place entered',
                        });
                    }

                }
            );
    }
})


.controller('QueryEntry', function($scope, $http, $ionicPopup, DataQ,$timeout, Query1ExtraInfo, $state) {
    console.log('show entered fields from button clicked-query 2');
    console.log(DataQ.getYear());
    console.log(DataQ.getPlace());
    $scope.persons = [];
    $scope.loading = true;
	var gotData=false;
    $http({
        method: 'GET',
        url: 'http://132.68.206.107:8080/Queries/Query2',
        //url: '/Queries/Query2',
        params: {
            place: DataQ.getPlace(),
            year: DataQ.getYear()
        }
    }).then(function successCallback(response) {
        console.log('success');
        $scope.persons = [];
        for (var r in response.data) {
            console.log(r);
            var person = response.data[r];
            console.log('url is ' + person.photoLink);
            if (person.photoLink == "") {
                person.photoLink = "http://www.freeiconspng.com/uploads/profile-icon-9.png";
            }
			
            var photoUrl = person.photoLink.replace(/\'/g, "\\'").replace(/\(/g, "\\(").replace(/\)/g, "\\)").replace(/\[/g, "\\[").replace(/\]/g, "\\]");
			person.photoLink = photoUrl;
			photoUrl = "url('" + person.photoLink + "')";
            person.photoLink = photoUrl;
			console.log('url is ' + person.photoLink);

            $scope.persons.push(person);
            console.log(person.name);
            console.log(person.wikiPageID);
            console.log(person);
            console.log(person.photoLink);

        }
		
        $scope.loading = false;
        console.log('end of success');
		gotData=true;
    }, function errorCallback(response) {
        alert(JSON.stringify(response))
        var FetchErrorAlert = $ionicPopup.alert({
            title: 'Fetch error!',
            template: 'Unable to get data',
        });
		gotData=true;
        console.log(response.data);
    });
	
	$timeout(function() {
          if (!gotData) {
            $ionicPopup.show({
                title: 'Don\'t worry',
                template: 'The query is still processing',
				 buttons: [ { text: '<b>Ok</b>' }]
            });
          }
        }, 3000);
	
    $scope.numberOfItemsToDisplay = 6; // Use it with limit to in ng-repeat
    $scope.addMoreItem = function(done) {
        if ($scope.persons.length > $scope.numberOfItemsToDisplay)
            $scope.numberOfItemsToDisplay += 6; // load number of more items
        $scope.$broadcast('scroll.infiniteScrollComplete')
    }

    $scope.showExtraInfo = function(per) {
        Query1ExtraInfo.setPerson(per);
        $state.go('app.extraInfo');
    };

})

.controller('Query1Entry', function($scope, $http, $ionicPopup, $timeout, Query1ExtraInfo, $state) {
    console.log('show entered fields from button clicked-query 1');
    $scope.persons = [];
    $scope.numberOfItemsToDisplay = 6; // Use it with limit to in ng-repeat

	var gotData=false;
    $scope.loading = true;

    console.log('started loading screen');

    $http({
        method: 'GET',
        url: 'http://132.68.206.107:8080/Queries/Query1',
        //url: '/Queries/Query1',
    }).then(function successCallback(response) {
        console.log('success');
        $scope.persons = [];
        for (var r in response.data) {
            var person = response.data[r];
            console.log('url is ' + person.photoLink);
            if (person.photoLink == "") {
                person.photoLink = "http://www.freeiconspng.com/uploads/profile-icon-9.png";
            }
            var photoUrl = person.photoLink.replace(/\'/g, "\\'").replace(/\(/g, "\\(").replace(/\)/g, "\\)").replace(/\[/g, "\\[").replace(/\]/g, "\\]");
			person.photoLink = photoUrl;
			photoUrl = "url('" + person.photoLink + "')";
            person.photoLink = photoUrl;

            $scope.persons.push(person);
            console.log(person.name);
            console.log(person.birthPlace);
            console.log(person.deathPlace);
            console.log(person.photoLink);
        }
        $scope.loading = false;
		gotData=true;

    }, function errorCallback(response) {
        alert(JSON.stringify(response))
        var FetchErrorAlert = $ionicPopup.alert({
            title: 'Fetch error!',
            template: 'Unable to get data',
        });
        console.log(response.data);
		gotData=true;
    });


    $scope.addMoreItem = function(done) {
        if ($scope.persons.length > $scope.numberOfItemsToDisplay)
            $scope.numberOfItemsToDisplay += 6; // load number of more items
        $scope.$broadcast('scroll.infiniteScrollComplete')
    }

    $scope.showExtraInfo = function(per) {
        console.log('217 name is ' + per.name);
        Query1ExtraInfo.setPerson(per);
        $state.go('app.extraInfo');
    };
	
	$timeout(function() {
          if (!gotData) {
            $ionicPopup.show({
                title: 'Don\'t worry',
                template: 'The query is still processing',
				 buttons: [ { text: '<b>Ok</b>' }]
            });
          }
        }, 3000);


})

.controller('ExtraInfo1', function($scope, $http, $ionicPopup,$timeout, Query1ExtraInfo) {
    console.log('229 in extra info');
    $scope.loading = true;
    $scope.stateShow = true;
	$scope.titleOverview=false;
	$scope.somethingToShow=false;
    $scope.personalInformation = Query1ExtraInfo.getPerson();
	$scope.deathExists=false;
	if($scope.personalInformation.deathDate/* != "0171-02-12"*/)
		$scope.deathExists=true;
	
	var clicked= false; var gotPersonal=false;
	
	var photoLink = $scope.personalInformation.photoLink;
	/*$scope.personalInformation.photoLink=photoLink.replace("url('", "");
	photoLink = $scope.personalInformation.photoLink;
	$scope.personalInformation.photoLink=photoLink.replace("')", "");*/
	console.log('url is ' + $scope.personalInformation.photoLink);
	console.log('259 name is ' + $scope.personalInformation.name);
    $scope.showMoreInfo = function() {
		clicked=true;
        $scope.stateShow = false;
        console.log('281 in on click more info');
        console.log('name is '+$scope.personalInformation.name);
		var name=$scope.personalInformation.name;
        $http({
            method: 'GET',
            url: 'http://132.68.206.107:8080/Queries/PersonalInformation',
            //url: '/Queries/PersonalInformation',
            params: {
                name: name
            }
        }).then(function successCallback(response) {
            console.log('personal data - success');
			$scope.somethingToShow=true;
            $scope.moreInfo = response.data;
            console.log('url is ' + $scope.moreInfo.photoLink);
            console.log('birthPla$scope.personalInformation.namece is:' + $scope.moreInfo.birthPlace);
            if ($scope.moreInfo.photoLink == "") {
                $scope.moreInfo.photoLink = "http://www.freeiconspng.com/uploads/profile-icon-9.png";
            }
			//$scope.personalInformation.name
            $scope.loading = false;
			$scope.titleOverview=true;
			gotPersonal=true;

        }, function errorCallback(response) {
            //alert(JSON.stringify(response))
			gotPersonal=true;
            console.log('loading is ' + $scope.loading);
            console.log('stateShow is ' + $scope.stateShow);
            $scope.loading = false;
			if($scope.personalInformation.deathDate || $scope.personalInformation.occupation || $scope.personalInformation.SpouseName) {
				$scope.somethingToShow=true;
			} else {
				var FetchErrorAlert = $ionicPopup.alert({
					title: 'Sorry',
					template: 'No more personal infornation to show',
				});
			}
			
            console.log(response.data);
            //$state.go('app.InteractiveSearch');
        });
    };
	$timeout(function() {
          if (!gotPersonal && clicked) {
            $ionicPopup.show({
                title: 'Don\'t worry',
                template: 'The query is still processing',
				 buttons: [ { text: '<b>Ok</b>' }]
            });
          }
        }, 3000);
})

.controller('SameOccupationQuery', function($scope, $http,$timeout, $ionicPopup) {
    console.log('show entered fields from button SameOccupationQuery');
    $scope.persons = [];
    $scope.numberOfItemsToDisplay = 6; // Use it with limit to in ng-repeat

    $scope.loading = true;
	var gotData=false;

    console.log('started loading screen');

    $http({
        method: 'GET',
        url: 'http://132.68.206.107:8080/Queries/SameOccupationCouples',
        //url: '/Queries/SameOccupationCouples',
    }).then(function successCallback(response) {
        console.log('success');
        console.log('');
        
		console.log(JSON.stringify(response));
		console.log('');
        $scope.persons = [];
        for (var r in response.data) {
            var person = response.data[r];
            //console.log('url is ' + person.photoLink);
            if (person.photoLink == "") {
                person.photoLink = "http://www.freeiconspng.com/uploads/profile-icon-9.png";
            }
            var photoUrl = person.photoLink.replace(/\'/g, "\\'").replace(/\(/g, "\\(").replace(/\)/g, "\\)").replace(/\[/g, "\\[").replace(/\]/g, "\\]");
			person.photoLink = photoUrl;
			photoUrl = "url('" + person.photoLink + "')";
            person.photoLink = photoUrl;

			
			
            $scope.persons.push(person);
            console.log(person.name);
            console.log('the spouse of '+ person.name+' is ' + response.data[r].spouseName);
            console.log('occupation is '+ person.occupation);
            //console.log(person.photoLink);
        }
        $scope.loading = false;
		gotData=true;

    }, function errorCallback(response) {
        var FetchErrorAlert = $ionicPopup.alert({
            title: 'Fetch error!',
            template: 'Unable to get data',
        });
        console.log(response.data);
		gotData=true;
    });


    $scope.addMoreItem = function(done) {
        if ($scope.persons.length > $scope.numberOfItemsToDisplay)
            $scope.numberOfItemsToDisplay += 6; // load number of more items
        $scope.$broadcast('scroll.infiniteScrollComplete')
    }
	$timeout(function() {
          if (!gotData) {
            $ionicPopup.show({
                title: 'Don\'t worry',
                template: 'The query is still processing',
				 buttons: [ { text: '<b>Ok</b>' }]
            });
          }
        }, 3000);


})

.controller('FriendsCtrl', function($scope, $stateParams, $timeout, ionicMaterialInk, ionicMaterialMotion) {
    // Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.$parent.setHeaderFab('left');

    // Delay expansion$scope.personalInformation.name
    $timeout(function() {
        $scope.isExpanded = true;
        $scope.$parent.setExpanded(true);
    }, 300);

    // Set Motion
    ionicMaterialMotion.fadeSlideInRight();

    // Set Ink
    ionicMaterialInk.displayEffect();
})

.controller('ProfileCtrl', function($scope, $stateParams, $timeout, ionicMaterialMotion, ionicMaterialInk) {
    // Set Header$scope.personalInformation.name
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.isExpanded = false;
    $scope.$parent.setExpanded(false);
    $scope.$parent.setHeaderFab(false);

    // Set Motion
    $timeout(function() {
        ionicMaterialMotion.slideUp({
            selector: '.slide-up'
        });
    }, 300);

    $timeout(function() {
        ionicMaterialMotion.fadeSlideInRight({
            startVelocity: 3000
        });
    }, 700);

    // Set Ink
    ionicMaterialInk.displayEffect();
})

.controller('InteractiveSearchCtrl', function($scope, $stateParams, $timeout, ionicMaterialMotion, ionicMaterialInk) {
    // Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.isExpanded = false;
    $scope.$parent.setExpanded(false);
    $scope.$parent.setHeaderFab(false);

    // Set Motion
    $timeout(function() {
        ionicMaterialMotion.slideUp({
            selector: '.slide-up'
        });
    }, 300);

    $timeout(function() {
        ionicMaterialMotion.fadeSlideInRight({
            startVelocity: 3000
        });
    }, 700);

    // Set Ink
    ionicMaterialInk.displayEffect();
})

.controller('AddQueryCtrl', function($scope, $state,$http,$q ,$ionicPopup, DynamicParams,ambiguousNames, $ionicPopover) {
	
	//$state.go('app.dynamicInput');
	
	
	 var template = '<ion-popover-view>' + /*'<ion-header-bar>' +
      '<h1 class = "title">Popover Title</h1>' +
      '</ion-header-bar>'+*/ '<ion-content>' +
      'Custom search' + '</ion-content>' + '</ion-popover-view>';
	
    // Triggered on a button click, or some other target
   $scope.searchPopUp = function() {
    $scope.dynamicData = {};
	$scope.checked=false;
	   $state.go('app.dynamicInput');
	   
   }
})



.controller('ambiguitySolver', function($scope, $state,$http,$q ,$ionicPopup, DynamicParams,ambiguousNames) {
	console.log('Start to solve ambiguous names');
	$scope.persons = [];
	$scope.persons = ambiguousNames.getNames();
	console.log($scope.persons[0].toString());
	$scope.query = DynamicParams.getQuery();
	var FetchErrorAlert = $ionicPopup.alert({
                title: 'Ambiguity !',
                template: 'There are many entities with the same name. <br> Please choose the person you are referring to.',
            });
	$scope.RetryQuery = function(name){
			console.log('No ambiguities');
			console.log('Query was added: ' + DynamicParams.getQuery().toString());
			console.log('Person to look for in query: ' + name);
			DynamicParams.setName(name.toString());
			DynamicParams.setQuery(DynamicParams.getQuery().toString());
			$state.go('app.dynamicQueryResults');
		
	};
})

.controller('ambiguitySolverArrest', function($scope, $state,$http,$q ,$ionicPopup, ArrestsParams,ambiguousNames) {
	console.log('Start to solve ambiguous names');
	$scope.persons = [];
	$scope.persons = ambiguousNames.getNames();
	console.log($scope.persons[0].toString());
	//$scope.query = DynamicParams.getQuery();
	var FetchErrorAlert = $ionicPopup.alert({
                title: 'Ambiguity !',
                template: 'There are many entities with the same name. <br> Please choose the person you are referring to.',
            });
	$scope.RetryQuery = function(name){
			console.log('No ambiguities');
			//console.log('Query was added: ' + DynamicParams.getQuery().toString());
			console.log('Person to look for Arrests in query: ' + name);
			ArrestsParams.setName(name.toString());
			//DynamicParams.setQuery(DynamicParams.getQuery().toString());
			$state.go('app.ArrestsResults');
		
	};
})

.controller('ambiguitySolverAwards', function($scope, $state,$http,$q ,$ionicPopup, AwardsParams,ambiguousNames) {
	console.log('Start to solve ambiguous names');
	$scope.persons = [];
	$scope.persons = ambiguousNames.getNames();
	console.log($scope.persons[0].toString());
	//$scope.query = DynamicParams.getQuery();
	var FetchErrorAlert = $ionicPopup.alert({
                title: 'Ambiguity !',
                template: 'There are many entities with the same name. <br> Please choose the person you are referring to.',
            });
	$scope.RetryQuery = function(name){
			console.log('No ambiguities');
			//console.log('Query was added: ' + DynamicParams.getQuery().toString());
			console.log('Person to look for Arrests in query: ' + name);
			AwardsParams.setName(name.toString());
			//DynamicParams.setQuery(DynamicParams.getQuery().toString());
			$state.go('app.AwardsResults');
		
	};
})

.controller('AwardsParameters', function($scope, $state, $ionicPopup,$http, AwardsParams, CheckNameInput, ambiguousNames) {
	//TODO: add check of name param
    $scope.showAwardsResults = function(name) {
		console.log('Sending params for Awards query ');
        CheckNameInput.validateInput(name)
            .then(
                function(response) {
                    if (response == 'OK') {
						
						/*AwardsParams.setName(name)
						$state.go('app.AwardsResults');*/
						
						$http({
							method: 'GET',
							url: 'http://132.68.206.107:8080/Queries/checkAmbiguities',
							//url: '/Queries/checkAmbiguities',
							params: {
								name: name
							}
						}).then(function successCallback(response) {
							$scope.listOfPersons = [];
							console.log(response);
							if (!response.data) {
								console.log('No ambiguities');
								AwardsParams.setName(name);
								$state.go('app.AwardsResults');

							} else {

								for (var r in response.data) {
									var info = response.data[r];

									$scope.listOfPersons.push(info);
									console.log(info);
								}
								$scope.loading = false;
								ambiguousNames.setNames($scope.listOfPersons);
								//DynamicParams.setQuery(query);
								$state.go('app.solveAmbiguityAwards');
							}
						}, function errorCallback(response) {
							//res.ambiguitiesSolved=false;
							var InputErrorAlert = $ionicPopup.alert({
								title: 'Input error!',
								template: 'Either this person daoesn\'t exist in Wikipedia or you are having a spelling error.',
							});
						});
                    }
                },
                function(error) {
                    if (error == 'MISSING') {
                        //don't allow the user search unless he enters all inputs
                        var InputErrorAlert = $ionicPopup.alert({
                            title: 'Input error!',
                            template: 'Missing Input. Please insert valid person\'s name',
                        });
                    } else if (error == 'INVALIDNAME') {
                        var InputErrorAlert = $ionicPopup.alert({
                            title: 'Input error!',
                            template: 'Illegal Input. Please insert a valid person\'s name',
                        });
                    }
                }
            );
        }
})

.controller('DynamicQueryCtrl', function($scope, $http, $state, $timeout, $ionicPopup, ionicMaterialMotion, ionicMaterialInk, DynamicParams) {
    // Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.isExpanded = false;
    $scope.$parent.setExpanded(false);
    $scope.$parent.setHeaderFab(false);
    $scope.loading = true;
    $scope.loadindPersonalInfo = true;
    $scope.failed = false;
    console.log('Show results of Get dynamic query was called');
    $scope.information = [];
    $scope.name = DynamicParams.getName();
    $scope.queryName = DynamicParams.getQuery();
    console.log($scope.name);
	$scope.err=false;
	var gotPersonal=false; var gotDynamic=false;
    $http({
        method: 'GET',
        url: 'http://132.68.206.107:8080/Queries/Dynamic',
        //url: '/Queries/Dynamic',
        params: {
            name: DynamicParams.getName(),
            query: DynamicParams.getQuery()
        }
    }).then(function successCallback(response) {
        console.log('Dynamic query success');
		gotDynamic=true;
        $scope.information = [];
        if (response.data == null) {
            $scope.loading = false;
        } else {
            for (var r in response.data) {
                var info = response.data[r];

                $scope.information.push(info);
                console.log(info);
            }
            $scope.loading = false;
        }

    }, function errorCallback(response) {
        alert('Unable to get data - Person does not exist.');
		gotDynamic=true;
        $scope.failed = true;
        $state.go('app.InteractiveSearch');
    });

    //Get the personal data of the person:
    if ($scope.failed == false) {
        $http({
            method: 'GET',
            url: 'http://132.68.206.107:8080/Queries/PersonalInformation',
            //url: '/Queries/PersonalInformation',
            params: {
                name: DynamicParams.getName()
            }
        }).then(function successCallback(response) {
            console.log('personal data - success');
			gotPersonal=true;
            $scope.personalInformation = response.data;
            $scope.loadindPersonalInfo = false;
            console.log('url is ' + $scope.personalInformation.photoLink);
            console.log('name is' + name);
            $scope.name = $scope.personalInformation.name;
            console.log('birthPlace is:' + $scope.personalInformation.birthPlace);
            if ($scope.personalInformation.photoLink == "") {
                $scope.personalInformation.photoLink = "http://www.freeiconspng.com/uploads/profile-icon-9.png";
            }
			var photoUrl = $scope.personalInformation.photoLink.replace(/\'/g, "\\'").replace(/\(/g, "\\(").replace(/\)/g, "\\)").replace(/\[/g, "\\[").replace(/\]/g, "\\]");
			$scope.personalInformation.photoLink=photoUrl;
			/*photoUrl = "url('" + $scope.personalInformation.photoLink + "')";
            $scope.personalInformation.photoLink = photoUrl;*/
			$scope.err=true;

        }, function errorCallback(response) {
			gotPersonal=true;
            var FetchErrorAlert = $ionicPopup.alert({
                title: 'Sorry',
                template: 'No personal Information to show',
            });
            console.log(response.data);
            $scope.loadindPersonalInfo = false;
			
        });
    }
	
	$timeout(function() {
          if (!gotPersonal || !gotDynamic) {
            $ionicPopup.show({
                title: 'Don\'t worry',
                template: 'The query is still processing',
				 buttons: [ { text: '<b>Ok</b>' }]
            });
          }
        }, 3000);

    // Set Motion
    $timeout(function() {
        ionicMaterialMotion.slideUp({
            selector: '.slide-up'
        });
    }, 300);

    $timeout(function() {
        ionicMaterialMotion.fadeSlideInRight({
            startVelocity: 3000
        });
    }, 700);

    // Set Ink
    ionicMaterialInk.displayEffect();
})

.controller('AwardsCtrl', function($scope, $http, $state, $timeout, $ionicPopup, ionicMaterialMotion, ionicMaterialInk, AwardsParams) {
    // Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.isExpanded = false;
    $scope.$parent.setExpanded(false);
    $scope.$parent.setHeaderFab(false);
    $scope.loading = true;
    $scope.loadindPersonalInfo = true;
    $scope.failed = false;
	$scope.err=true;
    console.log('Show results of Get Awards was called');
    $scope.information = [];
    $scope.name = AwardsParams.getName();
	var gotPersonal=false; var gotAwards=false;
    console.log($scope.name);
    $http({
        method: 'GET',
        url: 'http://132.68.206.107:8080/Queries/Awards',
        //url: '/Queries/Awards',
        params: {
            name: AwardsParams.getName()
        }
    }).then(function successCallback(response) {
        console.log('awards success');
        $scope.information = [];
        for (var r in response.data) {
            var info = response.data[r];

            $scope.information.push(info);
            console.log(info);
        }
        $scope.loading = false;
		gotAwards=true;

    }, function errorCallback(response) {
        var FetchErrorAlert = $ionicPopup.alert({
            title: 'Fetch error!',
            template: 'Unable to get data - Person does not exist',
        });
        console.log(response.data);
        $scope.loading = false;
        $scope.failed = true;
		gotAwards=true;
        //$state.go('app.InteractiveSearch');	
    });

    //Get the personal data of the person:
    if ($scope.failed == false) {
        $http({
            method: 'GET',
            url: 'http://132.68.206.107:8080/Queries/PersonalInformation',
            //url: '/Queries/PersonalInformation',
            params: {
                name: AwardsParams.getName()
            }
        }).then(function successCallback(response) {
            console.log('personal data - success');
            $scope.personalInformation = response.data;
            $scope.loadindPersonalInfo = false;
            console.log('url is ' + $scope.personalInformation.photoLink);
            console.log('name is' + name);
            $scope.name = $scope.personalInformation.name;
            console.log('birthPlace is:' + $scope.personalInformation.birthPlace);
            if ($scope.personalInformation.photoLink == "") {
                $scope.personalInformation.photoLink = "http://www.freeiconspng.com/uploads/profile-icon-9.png";
            }
			gotPersonal=true;
			/*var photoUrl = $scope.personalInformation.photoLink.replace(/\'/g, "\\'");
			$scope.personalInformation=photoUrl;*/

        }, function errorCallback(response) {
            var FetchErrorAlert = $ionicPopup.alert({
                title: 'Fetch error!',
                template: 'Unable to get Extra personal Information',
            });
            console.log(response.data);
			gotPersonal=true;
            $scope.loadindPersonalInfo = false;
			$scope.err=false;
        });
    }
	
	$timeout(function() {
          if (!gotPersonal || !gotAwards) {
            $ionicPopup.show({
                title: 'Don\'t worry',
                template: 'The query is still processing',
				 buttons: [ { text: '<b>Ok</b>' }]
            });
          }
        }, 3000);

    // Set Motion
    $timeout(function() {
        ionicMaterialMotion.slideUp({
            selector: '.slide-up'
        });
    }, 300);

    $timeout(function() {
        ionicMaterialMotion.fadeSlideInRight({
            startVelocity: 3000
        });
    }, 700);

    // Set Ink
    ionicMaterialInk.displayEffect();
})

.controller('ArrestsParameters', function($scope, $state, $ionicPopup,$http, ArrestsParams, CheckNameInput, ambiguousNames) {
    $scope.showArrestsResults = function(name) {
		console.log('Sending params for Arrests query ');
        CheckNameInput.validateInput(name)
            .then(
                function(response) {
                    if (response == 'OK') {
						
						/*ArrestsParams.setName(name)
            			$state.go('app.ArrestsResults');*/
						
						$http({
							method: 'GET',
							url: 'http://132.68.206.107:8080/Queries/checkAmbiguities',
							//url: '/Queries/checkAmbiguities',
							params: {
								name: name
							}
						}).then(function successCallback(response) {
							$scope.listOfPersons = [];
							console.log(response);
							if (!response.data) {
								console.log('No ambiguities');
								//console.log('Query was added: ' + query);
								console.log('Person to look for in Arrests query: ' + name);
								ArrestsParams.setName(name);
								//DynamicParams.setQuery(query);
								$state.go('app.ArrestsResults');

							} else {

								for (var r in response.data) {
									var info = response.data[r];

									$scope.listOfPersons.push(info);
									console.log(info);
								}
								$scope.loading = false;
								ambiguousNames.setNames($scope.listOfPersons);
								//DynamicParams.setQuery(query);
								$state.go('app.solveAmbiguityArrests');
							}
						}, function errorCallback(response) {
							var InputErrorAlert = $ionicPopup.alert({
								title: 'Input error!',
								template: 'The name you\'re entering may have a spelling error',
							});
						});
                    }
                },
                function(error) {
                    if (error == 'MISSING') {
                        //don't allow the user search unless he enters all inputs
                        var InputErrorAlert = $ionicPopup.alert({
                            title: 'Input error!',
                            template: 'Missing Input. Please insert valid person\'s name',
                        });
                    } else if (error == 'INVALIDNAME') {
                        var InputErrorAlert = $ionicPopup.alert({
                            title: 'Input error!',
                            template: 'Illegal Input. Please insert a valid person\'s name',
                        });
                    }

                }
            );
    };
})

.controller('Dynamicparameters', function($scope, $http, $ionicPopup, DynamicParams, $state,ambiguousNames, CheckDynamicParams) {
    $scope.showDynamicResults = function(query,person) {
		CheckDynamicParams.validateInput(query,person)
            .then(
                function(response) {
                    if (response == 'OK') {
						console.log('Input ok');
                       $http({
							method: 'GET',
							url: 'http://132.68.206.107:8080/Queries/checkAmbiguities',
							//url: '/Queries/checkAmbiguities',
							params: {
								name: person
							}
						}).then(function successCallback(response) {
							$scope.listOfPersons = [];
							console.log(response);
							if (!response.data) {
								console.log('No ambiguities');
								console.log('Query was added: ' + query);
								console.log('Person to look for in query: ' + person);
								DynamicParams.setName(person);
								DynamicParams.setQuery(query);
								$state.go('app.dynamicQueryResults');

							} else {

								for (var r in response.data) {
									var info = response.data[r];

									$scope.listOfPersons.push(info);
									console.log(info);
								}
								$scope.loading = false;
								ambiguousNames.setNames($scope.listOfPersons);
								DynamicParams.setQuery(query);
								$state.go('app.solveAmbiguity');
							}
						}, function errorCallback(response) {
							var InputErrorAlert = $ionicPopup.alert({
								title: 'Input error!',
								template: 'This person daoesn\'t exist in Wikipedia. Check for a spelling error',
							});
						});
                    }
                },
                function(error) {
                    if (error == 'MISSING') {
                        //don't allow the user search unless he enters all inputs
                        var InputErrorAlert = $ionicPopup.alert({
                            title: 'Input error!',
                            template: 'Missing Input. Please insert valid query and name',
                        });
                    } else if (error == 'INVALIDQUERY' || error == 'INVALIDSPACE') {
						console.log('error is '+error);
                        var InputErrorAlert = $ionicPopup.alert({
                            title: 'Input error!',
                            template: 'Illegal Input. Please insert a one word query which contains only letters',
                        });
                    } else if (error == 'INVALIDNAME') {
                        var InputErrorAlert = $ionicPopup.alert({
                            title: 'Input error!',
                            template: 'Illegal Input. Please insert a valid person\'s name',
                        });
                    }
					

                }
        );
	}
})


.controller('ArrestsCtrl', function($scope, $http, $state, $timeout, $ionicPopup, ionicMaterialMotion, ionicMaterialInk, ArrestsParams) {
    // Set Header
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.isExpanded = false;
    $scope.$parent.setExpanded(false);
    $scope.$parent.setHeaderFab(false);
    $scope.loading = true;
    $scope.loadindPersonalInfo = true;
    $scope.failed = false;
    console.log('Show results of Get Arrests was called');
    $scope.information = [];
    $scope.name = ArrestsParams.getName();
    console.log($scope.name);
	$scope.err=true;
	var gotPersonal =false; var gotArrested=false; //these varaibles are for tmeout purpuses
    $http({
        method: 'GET',
        url: 'http://132.68.206.107:8080/Queries/Arrests',
        //url: '/Queries/Arrests',
        params: {
            name: ArrestsParams.getName()
        }
    }).then(function successCallback(response) {
        console.log('success');
        $scope.information = [];
        for (var r in response.data) {
            var info = response.data[r];

            $scope.information.push(info);
            console.log(info);
        }
		
        $scope.loading = false;
		gotArrested=true;

    }, function errorCallback(response) {

        var FetchErrorAlert = $ionicPopup.alert({
            title: 'Fetch error!',
            template: 'Unable to get data - Person does not exist',
        });
        console.log(response.data);
        $scope.failed = true;
        $state.go('app.InteractiveSearch');
		gotArrested=true;
    });

    //Get the personal data of the person:
    if ($scope.failed == false /*&& $scope.loading == false*/) {
        $http({
            method: 'GET',
            url: 'http://132.68.206.107:8080/Queries/PersonalInformation',
            //url: '/Queries/PersonalInformation',
            params: {
                name: ArrestsParams.getName()
            }
        }).then(function successCallback(response) {
            console.log('personal data - success');
            $scope.personalInformation = response.data;
            $scope.loadindPersonalInfo = false;
            console.log('url is ' + $scope.personalInformation.photoLink);
            console.log('name is' + $scope.name);
            console.log('birthPlace is:' + $scope.personalInformation.birthExpandedPlace);
            if ($scope.personalInformation.photoLink == "") {
                $scope.personalInformation.photoLink = "http://www.freeiconspng.com/uploads/profile-icon-9.png";
            }
			var photoUrl = $scope.personalInformation.photoLink.replace(/\'/g, "\\'").replace(/\(/g, "\\(").replace(/\)/g, "\\)").replace(/\[/g, "\\[").replace(/\]/g, "\\]");
			$scope.personalInformation.photoLink=photoUrl;
			/*photoUrl = "url('" + $scope.personalInformation.photoLink + "')";
            $scope.personalInformation.photoLink = photoUrl;*/
			gotPersonal =true;

        }, function errorCallback(response) {
            var FetchErrorAlert = $ionicPopup.alert({
                title: 'Fetch error!',
                template: 'Unable to get Extra personal Information',
            });
            console.log(response.data);
            $scope.loadindPersonalInfo = false;
			gotPersonal =true;
			$scope.err=false;
        });
    }
	
	$timeout(function() {
          if (!gotPersonal || !gotArrested) {
            $ionicPopup.show({
                title: 'Don\'t worry',
                template: 'The query is still processing',
				 buttons: [ { text: '<b>Ok</b>' }]
            });
          }
        }, 3000);

    // Set Motion
    $timeout(function() {
        ionicMaterialMotion.slideUp({
            selector: '.slide-up'
        });
    }, 300);

    $timeout(function() {
        ionicMaterialMotion.fadeSlideInRight({
            startVelocity: 3000
        });
    }, 700);

    // Set Ink
    ionicMaterialInk.displayEffect();
})

.controller('ActivityCtrl', function($scope, $stateParams, $timeout, ionicMaterialMotion, ionicMaterialInk) {
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.isExpanded = true;
    $scope.$parent.setExpanded(true);
    $scope.$parent.setHeaderFab('right');

    $timeout(function() {
        ionicMaterialMotion.fadeSlideIn({
            selector: '.animate-fade-slide-in .item'
        });
    }, 200);

    // Activate ink for controller
    ionicMaterialInk.displayEffect();
})

.controller('GalleryCtrl', function($scope, $stateParams, $timeout, ionicMaterialInk, ionicMaterialMotion) {
    $scope.$parent.showHeader();
    $scope.$parent.clearFabs();
    $scope.isExpanded = true;
    $scope.$parent.setExpanded(true);
    $scope.$parent.setHeaderFab(false);

    // Activate ink for controller
    ionicMaterialInk.displayEffect();

    ionicMaterialMotion.pushDown({
        selector: '.push-down'
    });
    ionicMaterialMotion.fadeSlideInRight({
        selector: '.animate-fade-slide-in .item'
    });

})

;
