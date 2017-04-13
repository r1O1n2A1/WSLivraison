(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('CommandController', CommandController);

    CommandController.$inject = ['Command'];

    function CommandController(Command) {

        var vm = this;

        vm.commands = [];

        loadAll();

        function loadAll() {
            Command.query(function(result) {
                vm.commands = result;
                vm.searchQuery = null;
            });
        }
    }
})();
