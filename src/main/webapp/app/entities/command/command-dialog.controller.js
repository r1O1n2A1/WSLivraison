(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('CommandDialogController', CommandDialogController);

    CommandDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Command', 'Address', 'Postman', 'ShippingMethod'];

    function CommandDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Command, Address, Postman, ShippingMethod) {
        var vm = this;

        vm.command = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.addresses = Address.query();
        vm.postmen = Postman.query();
        vm.shippingmethods = ShippingMethod.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.command.id !== null) {
                Command.update(vm.command, onSaveSuccess, onSaveError);
            } else {
                Command.save(vm.command, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('wsLivraisonApp:commandUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateOrder = false;
        vm.datePickerOpenStatus.dateTaken = false;
        vm.datePickerOpenStatus.dateShipping = false;
        vm.datePickerOpenStatus.dateDelivery = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
