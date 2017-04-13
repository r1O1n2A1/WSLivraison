(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('ShippingMethodDetailController', ShippingMethodDetailController);

    ShippingMethodDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ShippingMethod', 'Command'];

    function ShippingMethodDetailController($scope, $rootScope, $stateParams, previousState, entity, ShippingMethod, Command) {
        var vm = this;

        vm.shippingMethod = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('wsLivraisonApp:shippingMethodUpdate', function(event, result) {
            vm.shippingMethod = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
