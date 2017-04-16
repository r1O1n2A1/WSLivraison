(function() {
	'use strict';
	angular
	.module('wsLivraisonApp')
	.controller('CommandWineController', CommandWineController);

	CommandWineController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Command', 'Address', 'Postman', 'ShippingMethod'];

	function CommandWineController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Command, Address, Postman, ShippingMethod) {
		var vm = this;
		var url = window.location.href;
		vm.clear = clear;
		vm.command = entity;

		vm.idCommand = parseUrl();
		
		function parseUrl() {
			var arrayUrl = url.split("/");
			if(arrayUrl.length !== 0) {
				return arrayUrl[4];
			} else {
				return "";
			}
		}

		function clear() {
			$uibModalInstance.dismiss('cancel');
		}
	}	

})();