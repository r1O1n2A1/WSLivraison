(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('ShippingMethodController', ShippingMethodController);

    ShippingMethodController.$inject = ['ShippingMethod'];

    function ShippingMethodController(ShippingMethod) {

        var vm = this;

        vm.shippingMethods = [];

        loadAll();

        function loadAll() {
            ShippingMethod.query(function(result) {
                vm.shippingMethods = result;
                vm.searchQuery = null;
            });
        }
    }
})();
