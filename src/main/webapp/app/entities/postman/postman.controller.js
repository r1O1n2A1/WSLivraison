(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('PostmanController', PostmanController);

    PostmanController.$inject = ['Postman'];

    function PostmanController(Postman) {

        var vm = this;

        vm.postmen = [];

        loadAll();

        function loadAll() {
            Postman.query(function(result) {
                vm.postmen = result;
                vm.searchQuery = null;
            });
        }
    }
})();
