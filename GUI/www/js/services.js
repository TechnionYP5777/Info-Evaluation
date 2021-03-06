/* global angular, document, window */

// authors:Genia, Moshe
// since : 6/5/2017
'use strict';

angular.module('starter.services', [])

.factory('DataQ', function() {
    var year = {};
    var place = {};
    return {
        getYear: function() {
            return year.toString();
        },
        setYear: function(yearparameter) {
            year = yearparameter;
        },
        getPlace: function() {
            return place;
        },
        setPlace: function(placeparameter) {
            place = placeparameter;
        }
    };
})

.factory('CheckNameInput', function($q) {
    return {
        isName: function(n) {
            return n.match(/[A-Za-z]/g);
        },
        validateInput: function(name) {
            var deferred = $q.defer();
            if (!name) {
                deferred.reject('MISSING');
            } else {
                if (!this.isName(name)) {
                    deferred.reject('INVALIDNAME');
                }
            }

            deferred.resolve('OK');
            return deferred.promise;
        }
    };
})


.factory('CheckDynamicParams', function($q) {
    return {
        isName: function(n) {
            return n.match(/[A-Za-z]/g);
        },
		containsNonLetter: function(n) {
            return n.match(/^[a-zA-Z]+$/);
        },
        validateInput: function(query, name) {
            var deferred = $q.defer();
            if (!name || !query) {
                deferred.reject('MISSING');
            } else {
				if (query.includes(" ")) {
					deferred.reject('INVALIDSPACE');
				}
				
				if (!this.containsNonLetter(query)) {
                    deferred.reject('INVALIDQUERY');
                }
				
				if (!this.isName(name)) {
                    deferred.reject('INVALIDNAME');
                }
            }

            deferred.resolve('OK');
            return deferred.promise;
        }
    };
})


.factory('ArrestsParams', function() {
    var name = {};
    return {
        getName: function() {
            return name.toString();
        },
        setName: function(nameParameter) {
            name = nameParameter;
        }
    };
})

.factory('AwardsParams', function() {
    var name = {};
    return {
        getName: function() {
            return name.toString();
        },
        setName: function(nameParameter) {
            name = nameParameter;
        }
    };
})

.factory('DynamicParams', function() {
    var name = {};
    var query = {};
    return {
        getName: function() {
            return name.toString();
        },
        setName: function(nameParameter) {
            name = nameParameter;
        },
        getQuery: function() {
            return query.toString();
        },
        setQuery: function(queryParameter) {
            query = queryParameter;
        }

    };
})

.factory('Query1ExtraInfo', function() {
    var person = {};

    return {
        getPerson: function() {
            return person;
        },
        setPerson: function(perParameter) {
            person = perParameter;
        }
    };
})


.factory('Query2ExtraInfo', function() {
    var name = {};
    return {
        getName: function() {
            return name.toString();
        },
        setName: function(nameParameter) {
            name = nameParameter;
        }
    };
})

.factory('CheckQuery2Input', function($q) {
    return {
        isNumber: function(n) {
            return !isNaN(parseFloat(n)) && isFinite(n);
        },
        validateInput: function(place, year) {
            var deferred = $q.defer();
            if ((!year || !place)) {
                deferred.reject('MISSING');
            } else {
                if (!this.isNumber(year)) {
                    deferred.reject('INVALIDYEAR');
                }
            }

            deferred.resolve('OK');
            return deferred.promise;
        }
    };
})

.factory('ambiguousNames', function() {
    var names = [];
    return {
        getNames: function() {
            return names;
        },
        setNames: function(namesParameter) {
            names = namesParameter;
        }
    };
})

//EOF
;