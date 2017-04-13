(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('PriceTableDeleteController',PriceTableDeleteController);

    PriceTableDeleteController.$inject = ['$uibModalInstance', 'entity', 'PriceTable'];

    function PriceTableDeleteController($uibModalInstance, entity, PriceTable) {
        var vm = this;

        vm.priceTable = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PriceTable.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
