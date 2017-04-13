(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('shipping-method', {
            parent: 'entity',
            url: '/shipping-method',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'wsLivraisonApp.shippingMethod.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shipping-method/shipping-methods.html',
                    controller: 'ShippingMethodController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shippingMethod');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('shipping-method-detail', {
            parent: 'shipping-method',
            url: '/shipping-method/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'wsLivraisonApp.shippingMethod.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/shipping-method/shipping-method-detail.html',
                    controller: 'ShippingMethodDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('shippingMethod');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ShippingMethod', function($stateParams, ShippingMethod) {
                    return ShippingMethod.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'shipping-method',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('shipping-method-detail.edit', {
            parent: 'shipping-method-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shipping-method/shipping-method-dialog.html',
                    controller: 'ShippingMethodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ShippingMethod', function(ShippingMethod) {
                            return ShippingMethod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('shipping-method.new', {
            parent: 'shipping-method',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shipping-method/shipping-method-dialog.html',
                    controller: 'ShippingMethodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                label: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('shipping-method', null, { reload: 'shipping-method' });
                }, function() {
                    $state.go('shipping-method');
                });
            }]
        })
        .state('shipping-method.edit', {
            parent: 'shipping-method',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shipping-method/shipping-method-dialog.html',
                    controller: 'ShippingMethodDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ShippingMethod', function(ShippingMethod) {
                            return ShippingMethod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('shipping-method', null, { reload: 'shipping-method' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('shipping-method.delete', {
            parent: 'shipping-method',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/shipping-method/shipping-method-delete-dialog.html',
                    controller: 'ShippingMethodDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ShippingMethod', function(ShippingMethod) {
                            return ShippingMethod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('shipping-method', null, { reload: 'shipping-method' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
