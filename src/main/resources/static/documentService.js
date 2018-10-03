'use strict';
var App = angular.module('uploadApp',[]);

App.factory('docService', ['$http', '$q', 'urls', function ($http, $q, urls) {

            var factory = {
                saveDoc: saveDoc,
                findDoc: findDoc
            };

            return factory;

            function saveDoc(file) {
                var deferred = $q.defer();
                var formData = new FormData();
                formData.append('file', file);
				var url = "/doc/upload";
                $http.post(url, formData,{
                    transformRequest : angular.identity,
                    headers : {
                        'Content-Type' : undefined
                    }})
                    .then(
                        function (response) {
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            alert(errResponse.data.errorMessage);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            };

            function findDoc(docId) {
                var deferred = $q.defer();
				var url = "/doc";
                $http.get(url + '/'+docId)
                    .then(
                        function (response) {
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            alert(errResponse.data.errorMessage);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
        }
    ]);