(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('CommandDeleteController',CommandDeleteController);

    CommandDeleteController.$inject = ['$uibModalInstance', 'entity', 'Command'];

    function CommandDeleteController($uibModalInstance, entity, Command) {
        var vm = this;

        vm.command = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Command.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
