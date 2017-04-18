(function(){
	'use strict';

	angular
	.module('wsLivraisonApp')
	.controller('ShippingChoiceController', ShippingChoiceController);

	ShippingChoiceController.$inject = ['$interval','$window', '$timeout', '$scope', '$stateParams',
		'Command', 'Address', 'ShippingMethod', 'Postman', 'Principal'];

	function ShippingChoiceController($interval, $window, $timeout, $scope, $stateParams,
			Command, Address, ShippingMethod, Postman, Principal, myMap) {
		var vm = this;
		vm.command = {};
		vm.address = {};
		vm.postmenCalculate = [];
		vm.typeShipping = 'Package';
		vm.typesShipping = ['Package', 'Letter'];
		vm.settingsAccount = null;
		vm.weightShipping = [1,2,4,6,8,10,20,25,50,70];
		vm.descriptionShipping = '';
		vm.fromAddress = {
				num: '12',
				address:'cours Vauban',
				zip: '33390',
				country: 'France',
				city: 'Blaye'
		}
		var postmen =  Postman.query();

		vm.country_list = ["Afghanistan","Albania","Algeria","Andorra","Angola","Anguilla","Antigua &amp; Barbuda","Argentina","Armenia","Aruba","Australia","Austria","Azerbaijan","Bahamas"
			,"Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize","Benin","Bermuda","Bhutan","Bolivia","Bosnia &amp; Herzegovina","Botswana","Brazil","British Virgin Islands"
			,"Brunei","Bulgaria","Burkina Faso","Burundi","Cambodia","Cameroon","Canada","Cape Verde","Cayman Islands","Chad","Chile","China","Colombia","Congo","Cook Islands","Costa Rica"
			,"Cote D Ivoire","Croatia","Cruise Ship","Cuba","Cyprus","Czech Republic","Denmark","Djibouti","Dominica","Dominican Republic","Ecuador","Egypt","El Salvador","Equatorial Guinea"
			,"Estonia","Ethiopia","Falkland Islands","Faroe Islands","Fiji","Finland","France","French Polynesia","French West Indies","Gabon","Gambia","Georgia","Germany","Ghana"
			,"Gibraltar","Greece","Greenland","Grenada","Guam","Guatemala","Guernsey","Guinea","Guinea Bissau","Guyana","Haiti","Honduras","Hong Kong","Hungary","Iceland","India"
			,"Indonesia","Iran","Iraq","Ireland","Isle of Man","Israel","Italy","Jamaica","Japan","Jersey","Jordan","Kazakhstan","Kenya","Kuwait","Kyrgyz Republic","Laos","Latvia"
			,"Lebanon","Lesotho","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macau","Macedonia","Madagascar","Malawi","Malaysia","Maldives","Mali","Malta","Mauritania"
			,"Mauritius","Mexico","Moldova","Monaco","Mongolia","Montenegro","Montserrat","Morocco","Mozambique","Namibia","Nepal","Netherlands","Netherlands Antilles","New Caledonia"
			,"New Zealand","Nicaragua","Niger","Nigeria","Norway","Oman","Pakistan","Palestine","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Poland","Portugal"
			,"Puerto Rico","Qatar","Reunion","Romania","Russia","Rwanda","Saint Pierre &amp; Miquelon","Samoa","San Marino","Satellite","Saudi Arabia","Senegal","Serbia","Seychelles"
			,"Sierra Leone","Singapore","Slovakia","Slovenia","South Africa","South Korea","Spain","Sri Lanka","St Kitts &amp; Nevis","St Lucia","St Vincent","St. Lucia","Sudan"
			,"Suriname","Swaziland","Sweden","Switzerland","Syria","Taiwan","Tajikistan","Tanzania","Thailand","Timor L'Este","Togo","Tonga","Trinidad &amp; Tobago","Tunisia"
			,"Turkey","Turkmenistan","Turks &amp; Caicos","Uganda","Ukraine","United Arab Emirates","United Kingdom","United States","United States Minor Outlying Islands","Uruguay","Uzbekistan","Venezuela","Vietnam","Virgin Islands (US)"
			,"Yemen","Zambia","Zimbabwe"];

		var idCommandAvailable = $window.localStorage.getItem('idCommandWine');

		// ------ Functions -----//

		/**
		 * Store the "settings account" in a separate variable, and not in the shared "account" variable.
		 */
		var copyAccount = function (account) {
			return {
				activated: account.activated,
				email: account.email,
				firstName: account.firstName,
				langKey: account.langKey,
				lastName: account.lastName,
				login: account.login
			};
		};

		Principal.identity().then(function(account) {
			vm.settingsAccount = copyAccount(account);
		});

		loadCommand();
		function loadCommand() {
			if(idCommandAvailable) {
				// case with id -> return the choosen command
				Command.get({id: idCommandAvailable}).$promise.then(
						function(result) {
							vm.command = result;
							if (result) {
								Address.get({id: vm.command.addressId}).$promise.then(
										function(result){
											vm.address = result;
										});
							}
						});
			} 
		}	

		calculateCost(postmen);
		function calculateCost(postmen) {
			// estimate distance + cost with postmen
			//1) find lat/long for each shipping FROM/TO
			var latlongFrom = calculateDistanceGoogleAPI(vm.fromAddress);
			var latlongTo = calculateDistanceGoogleAPI(vm.address);
			//2) calculate distance 
			
		}

		var geocoder = new google.maps.Geocoder();		
		function calculateDistanceGoogleAPI(address) {
			var strAddress = address.num + ' ' + address.address + ' ' + address.city;
			geocoder.geocode( { 'address': strAddress}, function(results, status) {
				if (status == google.maps.GeocoderStatus.OK) {
					var latitude = results[0].geometry.location.lat();
					var longitude = results[0].geometry.location.lng();
					return [latitude,longitude];
				}
			});
		}
		// calculate radius + distance from lat/long
		function rad(x) {
			return x * Math.PI / 180;
		}

		function getDistance(p1, p2) {
			var R = 6378137; // Earth’s mean radius in meter
			var dLat = rad(p2.lat() - p1.lat());
			var dLong = rad(p2.lng() - p1.lng());
			var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
			Math.cos(rad(p1.lat())) * Math.cos(rad(p2.lat())) *
			Math.sin(dLong / 2) * Math.sin(dLong / 2);
			var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			var d = R * c;
			return d; // returns the distance in meter
		}
		
		// end shipping
	}
})();