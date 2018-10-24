(function () {

    'use strict';

    angular
        .module('app')
        .controller('PingController', pingController);

    pingController.$inject = ['$http', 'authService'];

    function pingController($http, authService) {

        var API_URL = 'http://localhost:3001/api';
        var vm = this;
        vm.auth = authService;
        vm.message = '';

        vm.securedPing = function() {
            vm.message = '';
            $http.get(API_URL + '/private').then(function(result) {
                vm.message = result.data.message;
            }, function(error) {
                vm.message = error.data.message || error.data;
            });
        }

        vm.securedScopedPing = function() {
            vm.message = '';
            $http.get(API_URL + '/private-scoped').then(function(result) {
                vm.message = result.data.message;
            }, function(error) {
                vm.message = error.data.message || error.data;
            });
        }
    }

})();