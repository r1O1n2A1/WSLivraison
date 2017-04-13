'use strict';

describe('Controller Tests', function() {

    describe('Address Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAddress, MockRegion, MockCommand;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAddress = jasmine.createSpy('MockAddress');
            MockRegion = jasmine.createSpy('MockRegion');
            MockCommand = jasmine.createSpy('MockCommand');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Address': MockAddress,
                'Region': MockRegion,
                'Command': MockCommand
            };
            createController = function() {
                $injector.get('$controller')("AddressDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'wsLivraisonApp:addressUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
