'use strict';

describe('Controller Tests', function() {

    describe('Command Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCommand, MockAddress, MockPostman, MockShippingMethod;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCommand = jasmine.createSpy('MockCommand');
            MockAddress = jasmine.createSpy('MockAddress');
            MockPostman = jasmine.createSpy('MockPostman');
            MockShippingMethod = jasmine.createSpy('MockShippingMethod');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Command': MockCommand,
                'Address': MockAddress,
                'Postman': MockPostman,
                'ShippingMethod': MockShippingMethod
            };
            createController = function() {
                $injector.get('$controller')("CommandDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'wsLivraisonApp:commandUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
