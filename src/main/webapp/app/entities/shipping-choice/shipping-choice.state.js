(function() {
	'use strict';

	angular
	.module('wsLivraisonApp')
	.config(stateConfig);

	stateConfig.$inject = ['$stateProvider'];

	function stateConfig($stateProvider) {
		$stateProvider
		.state('shipping', {
			parent: 'home',
			url: 'choice/shipping',
			data: {
				authorities: ['ROLE_USER'],
				pageTitle: 'wsLivraisonApp.global.shipping.title'
			},
			views: {
                'content@': {
                    templateUrl: 'app/entities/shipping-choice/shipping-choice.html',
                    controller: 'ShippingChoiceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                    $translatePartialLoader.addPart('postman');
                    $translatePartialLoader.addPart('command');
                    return $translate.refresh();
                }]
            }
		});
	}
})();