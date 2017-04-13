(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('postman', {
            parent: 'entity',
            url: '/postman',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'wsLivraisonApp.postman.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/postman/postmen.html',
                    controller: 'PostmanController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('postman');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('postman-detail', {
            parent: 'postman',
            url: '/postman/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'wsLivraisonApp.postman.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/postman/postman-detail.html',
                    controller: 'PostmanDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('postman');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Postman', function($stateParams, Postman) {
                    return Postman.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'postman',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('postman-detail.edit', {
            parent: 'postman-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/postman/postman-dialog.html',
                    controller: 'PostmanDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Postman', function(Postman) {
                            return Postman.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('postman.new', {
            parent: 'postman',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/postman/postman-dialog.html',
                    controller: 'PostmanDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                label: null,
                                maxPrice: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('postman', null, { reload: 'postman' });
                }, function() {
                    $state.go('postman');
                });
            }]
        })
        .state('postman.edit', {
            parent: 'postman',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/postman/postman-dialog.html',
                    controller: 'PostmanDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Postman', function(Postman) {
                            return Postman.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('postman', null, { reload: 'postman' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('postman.delete', {
            parent: 'postman',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/postman/postman-delete-dialog.html',
                    controller: 'PostmanDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Postman', function(Postman) {
                            return Postman.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('postman', null, { reload: 'postman' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
