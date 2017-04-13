(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('PostmanDeleteController',PostmanDeleteController);

    PostmanDeleteController.$inject = ['$uibModalInstance', 'entity', 'Postman'];

    function PostmanDeleteController($uibModalInstance, entity, Postman) {
        var vm = this;

        vm.postman = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Postman.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
