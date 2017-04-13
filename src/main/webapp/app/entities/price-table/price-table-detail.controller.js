(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('PriceTableDetailController', PriceTableDetailController);

    PriceTableDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PriceTable', 'Postman'];

    function PriceTableDetailController($scope, $rootScope, $stateParams, previousState, entity, PriceTable, Postman) {
        var vm = this;

        vm.priceTable = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('wsLivraisonApp:priceTableUpdate', function(event, result) {
            vm.priceTable = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
