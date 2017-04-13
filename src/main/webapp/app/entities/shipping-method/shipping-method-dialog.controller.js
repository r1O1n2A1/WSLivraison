(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('ShippingMethodDialogController', ShippingMethodDialogController);

    ShippingMethodDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ShippingMethod', 'Command'];

    function ShippingMethodDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ShippingMethod, Command) {
        var vm = this;

        vm.shippingMethod = entity;
        vm.clear = clear;
        vm.save = save;
        vm.commands = Command.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.shippingMethod.id !== null) {
                ShippingMethod.update(vm.shippingMethod, onSaveSuccess, onSaveError);
            } else {
                ShippingMethod.save(vm.shippingMethod, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('wsLivraisonApp:shippingMethodUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
