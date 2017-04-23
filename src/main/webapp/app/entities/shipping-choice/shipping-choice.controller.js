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
				zipCode: '33390',
				country: 'France',
				city: 'Blaye'
		}
		vm.postmen =  Postman.query();
		vm.distance = 0;
		vm.estimated = false;
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
										}).then(function(result){
											var from = [];
											var to = [];
											// estimate distance + cost with postmen
											//1) find lat/long for each shipping FROM/TO
											calculateDistanceGoogleAPI(vm.fromAddress).then(
													function(sortedArray) {				
														from = sortedArray;
														if (from[0] && from[1]) {
															calculateDistanceGoogleAPI(vm.address).then(function(sortedArray){
																to = sortedArray;
																//2) calculate distance
																vm.distance = getDistance(from, to)/1000; //[km]
																//3) define postmen
																var coeff = definePostmenCoeffDate();
																if ($window.localStorage.getItem('indexChoosenShipping') !== 0 &&
																		vm.postmen.length && vm.postmenCalculate.length == 0) {
																	vm.postmen.forEach(function(res){
																		var temp = res.maxPrice;
																		res.dateDelivery = randomDate(new Date(),
																				new Date(+(new Date()) + Math.floor(Math.random()*1000000000)));
																		res.maxPrice = temp*coeff;
																		vm.postmenCalculate.push(res);
																	});
																}

															})
														}
													}, function (err) {
														console.error('Uh oh! An error occurred!', err);
													});
										});
							}
						});
			} 
		}	

		vm.empty = function isEmpty(obj) {
			return Object.keys(obj).length === 0;
		}

		// -------- START SHIPPING --------- //

		var geocoder = new google.maps.Geocoder();
		vm.calculateCost = function () {
			var from = [];
			var to = [];
			// estimate distance + cost with postmen
			//1) find lat/long for each shipping FROM/TO
			calculateDistanceGoogleAPI(vm.fromAddress).then(
					function(sortedArray) {				
						from = sortedArray;
						if (from[0] && from[1]) {
							calculateDistanceGoogleAPI(vm.address).then(function(sortedArray){
								to = sortedArray;
								//2) calculate distance
								vm.distance = getDistance(from, to)/1000; //[km]
								//3) define postmen
								var coeff = definePostmenCoeffDate();
								if ($window.localStorage.getItem('indexChoosenShipping') !== 0 &&
										vm.postmen.length && vm.postmenCalculate.length == 0) {
									vm.postmen.forEach(function(res){
										var temp = res.maxPrice;
										res.dateDelivery = randomDate(new Date(),
												new Date(+(new Date()) + Math.floor(Math.random()*1000000000)));
										res.maxPrice = temp*coeff;
										vm.postmenCalculate.push(res);
									});
								}

							})
						}
					}, function (err) {
						console.error('Uh oh! An error occurred!', err);
					});
		}		

		function calculateDistanceGoogleAPI(address) {
			var strAddress =  address.city;
			var deferred = $.Deferred();
			geocoder.geocode( { 'address': strAddress}, function(results, status) {
				if (status === 'OK') {                   
					var latLngArray = [
						+results[0].geometry.location.lat(),
						+results[0].geometry.location.lng()
						];

					deferred.resolve(latLngArray);
				} else {
					deferred.reject(status);
				}          
			});           
			return deferred.promise();
		}

//		calculate radius + distance from lat/long
		function rad(x) {
			return x * Math.PI / 180;
		}

		function getDistance(p1, p2) {
			var R = 6378137; // Earth’s mean radius in meter
			var dLat = rad(p2[0] - p1[0]);
			var dLong = rad(p2[1] - p1[1]);
			var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
			Math.cos(rad(p1[0])) * Math.cos(rad(p2[0])) *
			Math.sin(dLong / 2) * Math.sin(dLong / 2);
			var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
			return R * c; // returns the distance in meter
		}

		function onSaveSuccess (result) {
			$scope.$emit('wsLivraisonApp:commandUpdate', result);
			vm.isSaving = false;
		}

		function onSaveError () {
			vm.isSaving = false;
		}		

		function defineShippingMethod(distance) {
			if (distance) {
				if(distance > 0 && distance <= 1000) {
					vm.command.shippingMethodId = 3;
				} else if (distance > 1000 && distance <= 2000) {
					vm.command.shippingMethodId = 4;
				} else if (distance > 2000 && distance <= 4000) {
					vm.command.shippingMethodId = 1;
				} else {
					vm.command.shippingMethodId = 2;
				}
				Command.update(vm.command, onSaveSuccess, onSaveError);
			}
		}

		function definePostmenCoeffDate() {
			if(vm.distance) {
				var coeff = 1;
				if(vm.distance > 0 && vm.distance <= 1000) {
					// nothing
				} else if (vm.distance > 1000 && vm.distance <= 2000) {
					coeff = 2;
				} else if (vm.distance > 2000 && vm.distance <= 4000) {
					coeff = 4;
				} else {
					coeff = 5;
				}
				return coeff;
			}
		}

//		vm.calculateCost();
		vm.estimate = function() {
			if (vm.postmenCalculate) {
				vm.estimated = true;
			}
		}

		vm.choosenShipping = $window.localStorage.getItem('indexChoosenShipping');
		vm.started = false;	
		vm.chooseShipping = function(index) {			
			if(vm.distance) {
				$window.localStorage.setItem('indexChoosenShipping',index);
				defineShippingMethod(vm.distance);
			}			
			vm.started = true;
		}

		// -------- /END SHIPPING --------- //

		// ----- CALANDAR PART ----------//

		vm.datePickerOpenStatus = {};
		vm.openCalendar = openCalendar;

		vm.datePickerOpenStatus.dateOrder = false;
		vm.datePickerOpenStatus.dateTaken = false;
		vm.datePickerOpenStatus.dateShipping = false;
		vm.datePickerOpenStatus.dateDelivery = false;

		function randomDate(start, end) {
			var date =  new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
			date.setHours(Math.floor(Math.random() * 4) + 8,
					Math.floor(Math.random() * 10) + 6,
					Math.floor(Math.random() * 4) + 8,
					Math.floor(Math.random() * 4) + 8);
			return date;
		}

		function openCalendar(date) {
			vm.datePickerOpenStatus[date] = true;
		}


		//---- PART VALIDATION PAGE -----//
		/* should be externalized in another controller */

		vm.validated = false;
		vm.showDetailTerms = function() {
			$window.scrollTo(0, 0);
			vm.validated = true;

		}

		vm.redirectWineApp = function() {
			window.location.replace('http://localhost:8081/Wine-Web/pages/checkout3payment.jsf');
		}

		// -------------END CONTROLLER ----------//
	}
})();