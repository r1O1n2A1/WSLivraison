(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('ShippingMethodDeleteController',ShippingMethodDeleteController);

    ShippingMethodDeleteController.$inject = ['$uibModalInstance', 'entity', 'ShippingMethod'];

    function ShippingMethodDeleteController($uibModalInstance, entity, ShippingMethod) {
        var vm = this;

        vm.shippingMethod = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ShippingMethod.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
