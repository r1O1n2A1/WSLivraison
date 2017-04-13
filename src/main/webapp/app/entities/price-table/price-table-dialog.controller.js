(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('PriceTableDialogController', PriceTableDialogController);

    PriceTableDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PriceTable', 'Postman'];

    function PriceTableDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PriceTable, Postman) {
        var vm = this;

        vm.priceTable = entity;
        vm.clear = clear;
        vm.save = save;
        vm.postmen = Postman.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.priceTable.id !== null) {
                PriceTable.update(vm.priceTable, onSaveSuccess, onSaveError);
            } else {
                PriceTable.save(vm.priceTable, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('wsLivraisonApp:priceTableUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
