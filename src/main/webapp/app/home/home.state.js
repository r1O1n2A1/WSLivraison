(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('home', {
            parent: 'app',
            url: '/',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', 
                	function ($translate,$translatePartialLoader) {
	                    $translatePartialLoader.addPart('home');
	                    return $translate.refresh();
                }]
            }
        })
        .state('command.new.wine', {
        	parent: 'app',
        	url: '/wine/{id}',
        	data: {
        		authorities: []
        	},
        	views: {
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', 
                	function ($translate,$translatePartialLoader) {
	                    $translatePartialLoader.addPart('command');
	                    return $translate.refresh();
                }]
            },
        	onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal){
        		$uibModal.open({
        			templateUrl: 'app/entities/command/command-wine.html',
        			controller: 'CommandWineController',
        			controllerAs: 'vm',
        			backdrop: 'static',
        			size: 'lg'
        		}).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
        	}]
        });
    }
})();
