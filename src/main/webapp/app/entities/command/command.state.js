(function() {
    'use strict';

    angular
        .module('wsLivraisonApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('command', {
            parent: 'entity',
            url: '/command',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'wsLivraisonApp.command.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/command/commands.html',
                    controller: 'CommandController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('command');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('command-detail', {
            parent: 'command',
            url: '/command/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'wsLivraisonApp.command.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/command/command-detail.html',
                    controller: 'CommandDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('command');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Command', function($stateParams, Command) {
                    return Command.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'command',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('command-detail.edit', {
            parent: 'command-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/command/command-dialog.html',
                    controller: 'CommandDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Command', function(Command) {
                            return Command.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('command.new', {
            parent: 'command',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/command/command-dialog.html',
                    controller: 'CommandDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateOrder: null,
                                dateTaken: null,
                                dateShipping: null,
                                dateDelivery: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('command', null, { reload: 'command' });
                }, function() {
                    $state.go('command');
                });
            }]
        })
        .state('command.edit', {
            parent: 'command',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/command/command-dialog.html',
                    controller: 'CommandDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Command', function(Command) {
                            return Command.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('command', null, { reload: 'command' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('command.delete', {
            parent: 'command',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/command/command-delete-dialog.html',
                    controller: 'CommandDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Command', function(Command) {
                            return Command.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('command', null, { reload: 'command' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
