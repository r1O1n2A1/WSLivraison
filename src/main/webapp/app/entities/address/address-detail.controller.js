(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('AddressDetailController', AddressDetailController);

    AddressDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Address', 'Region', 'Command'];

    function AddressDetailController($scope, $rootScope, $stateParams, previousState, entity, Address, Region, Command) {
        var vm = this;

        vm.address = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('wsLivraisonApp:addressUpdate', function(event, result) {
            vm.address = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
