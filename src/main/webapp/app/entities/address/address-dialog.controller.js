(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('AddressDialogController', AddressDialogController);

    AddressDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Address', 'Region', 'Command'];

    function AddressDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Address, Region, Command) {
        var vm = this;

        vm.address = entity;
        vm.clear = clear;
        vm.save = save;
        vm.regions = Region.query({filter: 'address-is-null'});
        $q.all([vm.address.$promise, vm.regions.$promise]).then(function() {
            if (!vm.address.regionId) {
                return $q.reject();
            }
            return Region.get({id : vm.address.regionId}).$promise;
        }).then(function(region) {
            vm.regions.push(region);
        });
        vm.commands = Command.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.address.id !== null) {
                Address.update(vm.address, onSaveSuccess, onSaveError);
            } else {
                Address.save(vm.address, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('wsLivraisonApp:addressUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
