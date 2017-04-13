(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('CommandDetailController', CommandDetailController);

    CommandDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Command', 'Address', 'Postman', 'ShippingMethod'];

    function CommandDetailController($scope, $rootScope, $stateParams, previousState, entity, Command, Address, Postman, ShippingMethod) {
        var vm = this;

        vm.command = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('wsLivraisonApp:commandUpdate', function(event, result) {
            vm.command = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
