(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('PostmanDialogController', PostmanDialogController);

    PostmanDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Postman', 'Command', 'PriceTable', 'Region'];

    function PostmanDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Postman, Command, PriceTable, Region) {
        var vm = this;

        vm.postman = entity;
        vm.clear = clear;
        vm.save = save;
        vm.commands = Command.query();
        vm.pricetables = PriceTable.query();
        vm.regions = Region.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.postman.id !== null) {
                Postman.update(vm.postman, onSaveSuccess, onSaveError);
            } else {
                Postman.save(vm.postman, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('wsLivraisonApp:postmanUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
