(function(){
	'use strict';

	angular
	.module('wsLivraisonApp')
	.controller('ShippingChoiceController', ShippingChoiceController);

	ShippingChoiceController.$inject = ['$window', '$timeout', '$scope', '$stateParams',
		'Command', 'Address', 'ShippingMethod'];

	function ShippingChoiceController($window, $timeout, $scope, $stateParams,
			Command, Address, ShippingMethod) {
		var vm = this;
		vm.command = {};
		var temp = $window.localStorage.getItem('idCommandWine');
		loadCommand();
		function loadCommand() {
			if(temp) {
				// case ot id -> return the choosen command
				Command.get({id : temp}).$promise.then(
						function(result) {
							vm.command = result;
						});
			} 
		}

	}
})();