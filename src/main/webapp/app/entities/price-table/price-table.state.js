(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('price-table', {
            parent: 'entity',
            url: '/price-table',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'wsLivraisonApp.priceTable.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/price-table/price-tables.html',
                    controller: 'PriceTableController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('priceTable');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('price-table-detail', {
            parent: 'price-table',
            url: '/price-table/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'wsLivraisonApp.priceTable.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/price-table/price-table-detail.html',
                    controller: 'PriceTableDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('priceTable');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PriceTable', function($stateParams, PriceTable) {
                    return PriceTable.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'price-table',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('price-table-detail.edit', {
            parent: 'price-table-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-table/price-table-dialog.html',
                    controller: 'PriceTableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PriceTable', function(PriceTable) {
                            return PriceTable.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('price-table.new', {
            parent: 'price-table',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-table/price-table-dialog.html',
                    controller: 'PriceTableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                price: null,
                                rangeLow: null,
                                rangeHigh: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('price-table', null, { reload: 'price-table' });
                }, function() {
                    $state.go('price-table');
                });
            }]
        })
        .state('price-table.edit', {
            parent: 'price-table',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-table/price-table-dialog.html',
                    controller: 'PriceTableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PriceTable', function(PriceTable) {
                            return PriceTable.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('price-table', null, { reload: 'price-table' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('price-table.delete', {
            parent: 'price-table',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/price-table/price-table-delete-dialog.html',
                    controller: 'PriceTableDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PriceTable', function(PriceTable) {
                            return PriceTable.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('price-table', null, { reload: 'price-table' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
