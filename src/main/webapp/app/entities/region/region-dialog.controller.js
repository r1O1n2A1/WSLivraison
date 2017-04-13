(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('RegionDialogController', RegionDialogController);

    RegionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Region', 'Postman', 'Address'];

    function RegionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Region, Postman, Address) {
        var vm = this;

        vm.region = entity;
        vm.clear = clear;
        vm.save = save;
        vm.postmen = Postman.query();
        vm.addresses = Address.query({filter: 'region-is-null'});
        $q.all([vm.region.$promise, vm.addresses.$promise]).then(function() {
            if (!vm.region.addressId) {
                return $q.reject();
            }
            return Address.get({id : vm.region.addressId}).$promise;
        }).then(function(address) {
            vm.addresses.push(address);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.region.id !== null) {
                Region.update(vm.region, onSaveSuccess, onSaveError);
            } else {
                Region.save(vm.region, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('wsLivraisonApp:regionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
