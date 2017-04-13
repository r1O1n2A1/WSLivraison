(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .controller('PostmanDetailController', PostmanDetailController);

    PostmanDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Postman', 'Command', 'PriceTable', 'Region'];

    function PostmanDetailController($scope, $rootScope, $stateParams, previousState, entity, Postman, Command, PriceTable, Region) {
        var vm = this;

        vm.postman = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('wsLivraisonApp:postmanUpdate', function(event, result) {
            vm.postman = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
