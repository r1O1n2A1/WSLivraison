(function() {
	'use strict';

	angular
	.module('wsLivraisonApp')
	.controller('HomeController', HomeController);

	HomeController.$inject = ['$scope', 'Principal', 'LoginService',
		'$state', '$interval'];

	function HomeController ($scope, Principal, LoginService,
			$state, $interval) {
		var vm = this;

		vm.account = null;
		vm.isAuthenticated = null;
		vm.login = LoginService.open;
		vm.register = register;
		// slider properties
		$scope.myInterval = 3000;
		$scope.noWrapSlides = false;
		$scope.active = 0;
		vm.slides = [ 
			{    	
				src: './img/img00.jpg',
				description: 'image world map'
			}, 
			{
				src: './img/img02.jpg',
				description: 'image ship2'
			},
			{
				src: './img/img03.jpg',
				description: 'image train'
			},
			{
				src: './img/img04.jpg',
				description: 'image truck'
			}
			];


		$scope.$on('authenticationSuccess', function() {
			getAccount();
		});
		$scope.countDownTimer = Math.round(15000/(60),1); //minutes
		// TODO add localStorage, ngStorage for session
		// or add a cookie
		countDown();
		function countDown() {
			$interval(function(){
				$scope.countDownTimer--;
			}, 60000);
		}
		
		
		
		getAccount();

		function getAccount() {
			Principal.identity().then(function(account) {
				vm.account = account;
				vm.isAuthenticated = Principal.isAuthenticated;
			});
		}
		function register () {
			$state.go('register');
		}
	}
})();
