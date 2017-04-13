(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('PriceTableController', PriceTableController);

    PriceTableController.$inject = ['PriceTable'];

    function PriceTableController(PriceTable) {

        var vm = this;

        vm.priceTables = [];

        loadAll();

        function loadAll() {
            PriceTable.query(function(result) {
                vm.priceTables = result;
                vm.searchQuery = null;
            });
        }
    }
})();
