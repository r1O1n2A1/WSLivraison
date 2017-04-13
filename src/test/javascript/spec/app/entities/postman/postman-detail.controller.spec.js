'use strict';

describe('Controller Tests', function() {

    describe('Postman Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPostman, MockCommand, MockPriceTable, MockRegion;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPostman = jasmine.createSpy('MockPostman');
            MockCommand = jasmine.createSpy('MockCommand');
            MockPriceTable = jasmine.createSpy('MockPriceTable');
            MockRegion = jasmine.createSpy('MockRegion');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Postman': MockPostman,
                'Command': MockCommand,
                'PriceTable': MockPriceTable,
                'Region': MockRegion
            };
            createController = function() {
                $injector.get('$controller')("PostmanDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'wsLivraisonApp:postmanUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
